package br.edu.ifsp.scl.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity : AppCompatActivity() {
    protected val EXTRA_TASK = "Task"
    protected val EXTRA_VIEW_TASK = "ViewTask"

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("731880517852-6obkl6krpi6iu0b3isppqgjm69q4poaq.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
}