import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.TeamApp.data.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    // Ustawienie użytkownika w ViewModel
    fun setUser(user: User) {
        _user.value = user
    }

    // Pobranie użytkownika z Firestore
    fun fetchUserFromFirestore(email: String) {
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val userData = documents.first().toObject(User::class.java)
                    setUser(userData)
                    Log.d("Fetchinguser", "User found: $userData")
                } else {
                    Log.e("Fetchinguser", "User not found")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserViewModel", "Error getting user from Firestore", exception)
            }
    }
}
