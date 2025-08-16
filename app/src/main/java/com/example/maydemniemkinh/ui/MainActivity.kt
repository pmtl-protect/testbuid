package com.example.maydemniemkinh.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.maydemniemkinh.R
import com.example.maydemniemkinh.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var guestCount = 0
    // Các biến trạng thái khác sẽ được thêm ở đây

    // ActivityResultLauncher để xử lý kết quả trả về từ màn hình đăng nhập Google
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("MainActivity", "Google sign in failed", e)
                Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo Firebase Auth
        auth = Firebase.auth

        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setupEventListeners()
    }

    override fun onStart() {
        super.onStart()
        // Kiểm tra xem người dùng đã đăng nhập chưa và cập nhật UI
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun setupEventListeners() {
        binding.btnIncrement.setOnClickListener { incrementCounter() }
        binding.tvCounter.setOnClickListener { incrementCounter() }
        binding.btnDecrement.setOnClickListener { decrementCounter() }
        
        binding.btnLogin.setOnClickListener { signIn() }

        // Thêm sự kiện cho nút đăng xuất (nếu có)
        // Ví dụ: binding.btnLogout.setOnClickListener { signOut() }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập Firebase thành công, cập nhật UI
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // Đăng nhập Firebase thất bại
                    Toast.makeText(this, "Xác thực Firebase thất bại.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    
    // Hàm đăng xuất (ví dụ)
    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            updateUI(null)
        }
    }

    private fun incrementCounter() {
        if (auth.currentUser != null) {
            // TODO: Logic cập nhật lên Firestore
        } else {
            guestCount++
            updateCounterDisplay()
        }
    }

    private fun decrementCounter() {
        if (auth.currentUser != null) {
            // TODO: Logic cập nhật lên Firestore
        } else {
            if (guestCount > 0) {
                guestCount--
                updateCounterDisplay()
            }
        }
    }
    
    private fun updateCounterDisplay() {
        val countToDisplay = if (auth.currentUser != null) {
            0 // TODO: Lấy số đếm từ dữ liệu người dùng
        } else {
            guestCount
        }
        binding.tvCounter.text = countToDisplay.toString()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Đăng nhập thành công
            binding.btnLogin.visibility = View.GONE
            binding.ivUserAvatar.visibility = View.VISIBLE
            binding.layoutSelection.visibility = View.VISIBLE
            
            // Dùng thư viện Glide để tải ảnh avatar
            Glide.with(this).load(user.photoUrl).into(binding.ivUserAvatar)
            
            // TODO: Bắt đầu lắng nghe dữ liệu từ Firestore
            
        } else {
            // Trạng thái khách
            binding.btnLogin.visibility = View.VISIBLE
            binding.ivUserAvatar.visibility = View.GONE
            binding.layoutSelection.visibility = View.INVISIBLE
            guestCount = 0
        }
        updateCounterDisplay()
    }
}
