import androidx.activity.result.IntentSenderRequest

interface SignInLauncher {
    fun launchSignIn(intent: IntentSenderRequest)
}