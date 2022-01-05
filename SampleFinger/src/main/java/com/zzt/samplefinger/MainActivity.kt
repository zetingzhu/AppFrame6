//package com.zzt.samplefinger
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import androidx.core.hardware.fingerprint.FingerprintManagerCompat
//
//class MainActivity : AppCompatActivity() {
//
//    /**
//     * A class that coordinates access to the fingerprint hardware.
//    On platforms before Build.VERSION_CODES.M, this class behaves as there would be no fingerprint hardware available.
//    Deprecated
//    Use androidx.biometrics.BiometricPrompt instead.
//     */
//    var fingerManger: FingerprintManagerCompat? = null
//
//    var biometricPrompt: BiometricPrompt? = null
//
//    var button: Button? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        initView()
//    }
//
//    private fun initView() {
//        button = findViewById(R.id.button)
//        button?.setOnClickListener {
//            val biometricManager = BiometricManager.from(this)
//            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
//                BiometricManager.BIOMETRIC_SUCCESS ->
//                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
//                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
//                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
//                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
//                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
//                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                    // Prompts the user to create credentials that your app accepts.
//                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                        putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
//                    }
//                    startActivityForResult(enrollIntent, REQUEST_CODE)
//                }
//            }
//        }
//    }
//
//}