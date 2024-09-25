package com.example.TeamApp.auth

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TeamApp.data.User
import com.example.TeamApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import androidx.navigation.NavController
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.data.Avatar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val _email = MutableLiveData("")
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    val email: LiveData<String> = _email

    private val _snackbarMessage = MutableLiveData<String?>()
    val snackbarMessage: LiveData<String?> get() = _snackbarMessage

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun setLoading(loading: Boolean) {
        Log.d("LoginViewModel", "setLoading: $loading")
        _isLoading.value = loading
    }
    fun mySetSignInLauncher(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        signInLauncher = launcher
        if (!::signInLauncher.isInitialized) {
            Log.e("LoginViewModel", "signInLauncher has not been initialized")
        }
        else{
            Log.d("LoginViewModel", "signInLauncher has been initialized")
        }
    }

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _loginSuccess = MutableLiveData<Boolean?>(null)
    val loginSuccess: LiveData<Boolean?> = _loginSuccess

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    private val _registerSuccess = MutableLiveData<Boolean?>(null)
    val registerSuccess: LiveData<Boolean?> = _registerSuccess

    private val _emailSent = MutableLiveData<Boolean?>(null)
    val emailSent: LiveData<Boolean?> = _emailSent

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(newUsername: String) {
        _email.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("register"){
            popUpTo("createEvent") { inclusive = true }
        }
    }

    fun onLoginClick(navController: NavController, callback: (String?) -> Unit) {
        val email = _email.value ?: return  callback("Uzupełnij wszystkie pola")
        val password = _password.value ?: return  callback("Uzupełnij wszystkie pola")
        setLoading(true)
        Log.d("LoginAttempt", "Attempting to log in with email: $email")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        setLoading(false)
                        if (task.isSuccessful) {
                            Log.d("Login", "Login successful")
                            _loginSuccess.value = true
                            callback(null)

                            val context = navController.context
                            val intent = Intent(context, MainAppActivity::class.java)
                            // Dodaj flagi, aby zamknąć bieżącą aktywność po przejściu do nowej
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        } else {
                            Log.e("Login", "Login failed: ${task.exception?.message}")
                            callback(task.exception?.message)
                            _loginSuccess.value = false
                        }
                    }, 200) // delay
                }
        } else {
            Log.d("LoginAttempt", "Login failed: empty email or password field")
            _loginSuccess.value= false
            setLoading(false)
            callback("Uzupełnij wszystkie pola")
        }
    }



    fun signInWithGoogle(context: Context) {
        if (!::signInLauncher.isInitialized) {
            Log.e("LoginViewModel", "signInLauncher has not been initialized")
            return
        }
        val clientId = context.getString(R.string.client_id)
        Log.d("LoginViewModel", "Client ID: $clientId")

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientId)
                    .setFilterByAuthorizedAccounts(false) // Allow all accounts
                    .build()
            )
            .build()

        Identity.getSignInClient(context).beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                Log.d("LoginViewModel", "Google Sign-In success: ${result.pendingIntent}")
                val intent = result.pendingIntent.intentSender
                signInLauncher.launch(IntentSenderRequest.Builder(intent).build())
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "Google Sign-In failed", e)
                if (e is ApiException) {
                    Log.e("LoginViewModel", "Status Code: ${e.statusCode}")
                }
            }
    }


    fun getToLoginScreen(navController: NavController) {
        navController.navigate("login")
    }

    fun getToRegisterScreen(navController: NavController) {
        navController.navigate("register")
    }

    fun getToChangePasswordScreen(navController: NavController){
        navController.navigate("forgotPassword") {
            popUpTo("login") { inclusive = true }
        }
    }

    fun getToSexChoiceScreen(navController: NavController){
        navController.navigate("sexChoice")
    }

    fun onRegisterClick(navController: NavController, callback: (String?) -> Unit){
        Log.e("LoginViewModel", "onRegisterClick")

        val email = _email.value ?: return callback("Uzupełnij wszystkie pola")
        val password = _password.value ?: return callback("Uzupełnij wszystkie pola")
        val confirmPassword = _confirmPassword.value ?: return callback("Uzupełnij wszystkie pola")
        // Temporarily hardcoded username
        val username = "xyz"
        val db = Firebase.firestore
        var arePasswordsDifferent: Boolean = false
        var errorMessage: String? = null

        if (email.isNotEmpty() && password.isNotEmpty()) {
            setLoading(true)  // Start loading spinner here
            if (password != confirmPassword) {
                Log.d("RegisterAttempt", "Register failed: passwords do not match")
                arePasswordsDifferent = true
                setLoading(false)
                return callback("Hasła są niezgodne")
            } else {
                Log.d("RegisterAttempt", "Attempting to register with email: $email")
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            if (task.isSuccessful && !arePasswordsDifferent) {
                                Log.d("Register", "Registration successful")

                                // Retrieve the Firebase UID and create User object
                                val firebaseUser = auth.currentUser
                                val firebaseUserId = firebaseUser?.uid ?: return@postDelayed

                                // Now create the user object with the Firebase UID
                                val user = User(name = username, email = email).apply {
                                    this.userID = firebaseUserId  // Set Firebase's user ID
                                }

                                // Save user data in Firestore with the Firebase userID
                                db.collection("users").document(firebaseUserId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "User successfully written!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Error writing user", e)
                                    }

                                _registerSuccess.value = true
                                callback(null)

//                                // Navigate to MainAppActivity
//                                val context = navController.context
//                                val intent = Intent(context, MainAppActivity::class.java)
//                                // Add flags to clear the current activity
//                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                context.startActivity(intent)
                                navController.navigate("sexName"){
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                Log.e("Register", "Registration failed: ${task.exception?.message}")
                                errorMessage = when (val exception = task.exception) {
                                    is FirebaseAuthWeakPasswordException -> "Hasło jest za słabe"
                                    is FirebaseAuthInvalidCredentialsException -> "Zły format e-maila"
                                    is FirebaseAuthUserCollisionException -> "Konto z podanym mailem już istnieje"
                                    else -> exception?.message ?: "Rejestracja nie powiodła się"
                                }
                                _registerSuccess.value = false
                                callback(errorMessage)
                            }
                            setLoading(false) // Stop loading spinner here, after all tasks are done
                        }, 300) // delay for better UX
                    }
            }
        } else {
            Log.d("RegisterAttempt", "Register failed: empty email or password field")
            _registerSuccess.value = false
            setLoading(false)
            callback("Uzupełnij wszystkie pola")
        }
    }


    fun resetSuccess() {
        _loginSuccess.postValue(null)
        _registerSuccess.postValue(null)
        _emailSent.postValue(null)
    }

    fun onForgotPasswordClick(callback: (String?) -> Unit) {
        val email = _email.value ?: return
        if (email.isNullOrEmpty()) {
            Log.e("LoginViewModel", "Email is empty")
            _emailSent.value = false // Indicate that the operation was unsuccessful
            return callback("Uzupełnij pole")
        }

        Log.d("LoginViewModel", "Checking if email exists in Firestore: $email")
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Email exists in the database, proceed with password reset
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                _emailSent.value = true
                                Log.d("LoginViewModel", "Password reset email sent.")
                                callback(null)
                            } else {
                                _emailSent.value = false
                                Log.e("LoginViewModel", "Failed to send password reset email.", sendTask.exception)
                                callback("Nie udało się wysłać maila")
                            }
                        }
                } else {
                    // Email does not exist in the database
                    _emailSent.value = false
                    Log.e("LoginViewModel", "Email not found in the database.")
                    callback("Nie znaleziono konta z podanym mailem")
                }
            }
            .addOnFailureListener { exception ->
                _emailSent.value = false
                Log.e("LoginViewModel", "Error checking email in the database.", exception)
                callback("Błąd sprawdzania maila w bazie danych")
            }
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _avatars = MutableLiveData<List<Avatar>>(emptyList())
    val avatars: LiveData<List<Avatar>> get() = _avatars

    private val _selectedAvatarIndex = MutableLiveData(0)
    val selectedAvatarIndex: LiveData<Int> get() = _selectedAvatarIndex

    private var _loadedCount = 0
    private val TOTAL_AVATARS_COUNT = 25

    fun selectAvatarByIndex(index: Int) {
        _selectedAvatarIndex.value = index
    }

    init {

        _user.value = User(
            name = "Jan",
            email = "jan@example.com",
            birthDay = "01/01/1990",
            gender = "Female",
            avatar = "null"
        )
    }

    fun loadAvatars(limit: Int) {
        if (_avatars.value?.isNotEmpty() == true) {
            Log.d("AvatarLoading", "Awatary są już załadowane.")
            return
        }
        val storage = FirebaseStorage.getInstance()
        //val limit = minOf(_loadedCount + 10, TOTAL_AVATARS_COUNT)
        if(limit <= TOTAL_AVATARS_COUNT){
            viewModelScope.launch {
                val loadedAvatars = mutableListOf<Avatar>() // Temporary list to store loaded avatars

                for (i in (_loadedCount + 1)..limit) {

                    val faceRef: StorageReference
                    val avatarRef: StorageReference

                    if (_user.value!!.gender == "Male") {
                        faceRef = storage.reference.child("facesMen/face$i.png")
                        avatarRef = storage.reference.child("avatarsMen/avatar$i.png")
                    } else {
                        faceRef = storage.reference.child("facesWomen/face$i.png")
                        avatarRef = storage.reference.child("avatarsWomen/avatar$i.png")
                    }

                    Log.d("AvatarLoading", "Loading face from: ${faceRef.path}")
                    Log.d("AvatarLoading", "Loading avatar from: ${avatarRef.path}")

                    try {
                        val faceUrl = faceRef.downloadUrl.await()
                        val avatarUrl = avatarRef.downloadUrl.await()

                        Log.d("Avatar", "Face URL: $faceUrl, Avatar URL: $avatarUrl")

                        val avatar = Avatar(
                            faceUrl = faceUrl.toString(),
                            avatarUrl = avatarUrl.toString()
                        )

                        loadedAvatars.add(avatar) // Add to temporary list
                    } catch (e: Exception) {
                        Log.e("AvatarLoading", "Error loading avatar$i: ${e.message}")
                    }
                }

                // After loading, update LiveData
                _avatars.value = _avatars.value?.plus(loadedAvatars) ?: loadedAvatars
                _loadedCount += loadedAvatars.size // Increment loadedCount by the number of avatars loaded
            }
        }
    }



    fun saveSelectedAvatar(avatarUrl: String) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//        val db = FirebaseFirestore.getInstance()
//        val userRef = db.collection("users").document(userId)
//
//        // Sprawdź, czy dokument istnieje
//        userRef.get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    // Dokument istnieje, zaktualizuj avatar
//                    userRef.update("avatar", avatarUrl)
//                        .addOnSuccessListener {
//                            Log.d("Firestore", "Avatar updated for user $userId")
//                        }
//                        .addOnFailureListener { e ->
//                            Log.e("Firestore", "Error updating avatar: ${e.message}")
//                        }
//                } else {
//                    // Dokument nie istnieje, zgłoś błąd
//                    Log.e("Firestore", "User document not found for user ID: $userId")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error fetching user document: ${e.message}")
//            }
        _user.value!!.avatarUrl = avatarUrl
        Log.d("USER","avatarURL: ${_user.value!!.avatarUrl}")
    }
}