package com.fwhyn.connectivity

//class BluetoothViewModel : AppCompatActivity() {
//    private val bluetoothCheck = BluetoothCheck(this, this)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bluetooth)
//
//        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
//            bluetoothCheck.bleCheck(object : BluetoothCheckCallback {
//                override fun ableToScan() {
//                    Toast.makeText(this@BluetoothViewModel, "Ok", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//                override fun unableToScan(reason: BluetoothCheckCallback.Reason) {
//                    Toast.makeText(this@BluetoothViewModel, reason.toString(), Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//            })
//        }
//    }
//}