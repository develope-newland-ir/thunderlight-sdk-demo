package com.thunderlight.sdkDemo.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.thunderlight.sdkDemo.constant.ConstantsStr
import com.thunderlight.sdkDemo.constant.ConstantsStr.POS_DATA
import com.thunderlight.sdkDemo.constant.ConstantsStr.TRANSACTION_DATA
import com.thunderlight.sdkDemo.constant.getVersion
import com.thunderlight.sdkDemo.ui.service.MainServicesAdapter
import com.thunderlight.sdkDemo.ui.service.MainServicesViewModel
import com.thunderlight.sdkDemo.ui.service.model.MainServicesItem
import com.thunderlight.sdkdemo.R
import com.thunderlight.sdkdemo.databinding.ActivityMainBinding
import com.thunderlight.thundersmartsdk.constant.RequestType
import com.thunderlight.thundersmartsdk.constant.TxnInquiryType
import com.thunderlight.thundersmartsdk.data.PosData
import com.thunderlight.thundersmartsdk.data.TransactionData
import com.thunderlight.thundersmartsdk.generalManager.GeneralSDKManager
import com.thunderlight.thundersmartsdk.generalManager.PosDataCallBack
import com.thunderlight.thundersmartsdk.generalManager.ResultCallBack
import com.thunderlight.thundersmartsdk.generalManager.TransactionCallBack
import java.util.*


open class MainServicesActivity : AppCompatActivity() {

    private val TAG = "MainServicesActivity"
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainServicesViewModel by viewModels()
    private val serviceAdapter by lazy { MainServicesAdapter() }

    //define sdkManager Instance
    private val sdkManager = GeneralSDKManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initViewModel()
    }

    private fun initData() {
        //init sdkManager
        val host = sdkManager.init()

        binding.apply {
            header.tvName.text = String.format(Locale.CANADA, getString(R.string.version_name, getVersion(this@MainServicesActivity)))
            header.tvHostName.text = String.format(Locale.CANADA, getString(R.string.host_name, host.value))
        }
    }

    private fun initViewModel() {
        viewModel.menuList.observe(this@MainServicesActivity) {
            initRecyclerView(it)
        }
        viewModel.getMenuItems()
    }

    private fun initRecyclerView(appList: ArrayList<MainServicesItem>) {
        binding.apply {
            recyclerView.apply {
                serviceAdapter.setData(appList)
                layoutManager = GridLayoutManager(context, 2)
                adapter = serviceAdapter


                val transactionCallBack = object : TransactionCallBack {
                    override fun onReceive(transactionData: TransactionData) {
                        Log.i(TAG, "transactionCallBack onReceive: $transactionData")//call toString() of transactionData
                        val intent = Intent(this@MainServicesActivity, ResultActivity::class.java)
                        intent.putExtra(TRANSACTION_DATA, transactionData)
                        startActivity(intent)
                    }

                    override fun onError(errorCode: String, errorMsg: String) {
                        Log.i(TAG, "transactionCallBack onError: $errorCode :  $errorMsg ")
                        Toast.makeText(this@MainServicesActivity, "error: $errorCode : $errorMsg ", Toast.LENGTH_LONG).show()
                    }

                    override fun onCanceled() {
                        Log.i(TAG, "transactionCallBack onCanceled: ")
                        Toast.makeText(this@MainServicesActivity, "Canceled", Toast.LENGTH_LONG).show()
                    }
                }

                val posDataCallBack = object : PosDataCallBack {
                    override fun onReceive(posData: PosData) {
                        Log.i(TAG, "posDataCallBack onReceive: $posData")
                        val intent = Intent(this@MainServicesActivity, ResultActivity::class.java)
                        intent.putExtra(POS_DATA, posData)
                        startActivity(intent)
                    }

                    override fun onError(errorCode: String, errorMsg: String) {
                        Log.i(TAG, "posDataCallBack onError: $errorCode :  $errorMsg ")
                        Toast.makeText(this@MainServicesActivity, "error: $errorCode : $errorMsg ", Toast.LENGTH_LONG).show()
                    }
                }

                val resultCallBack = object : ResultCallBack {
                    override fun onSuccess() {
                        Log.i(TAG, "keyChangeCallBack onSuccess ")
                        Toast.makeText(this@MainServicesActivity, "--------- Result Success ---------", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(errorCode: String, errorMsg: String) {
                        Log.i(TAG, "keyChangeCallBack onError: $errorCode :  $errorMsg ")
                        Toast.makeText(this@MainServicesActivity, "error: $errorCode : $errorMsg ", Toast.LENGTH_LONG).show()
                    }
                }

                serviceAdapter.setOnItemClickListener { order, txnType ->
                    if (order == ConstantsStr.ACTION_OPEN) {
                        when (txnType) {
                            RequestType.REQUEST_TYPE_BALANCE -> {
                                sdkManager.inquiryBalance(this@MainServicesActivity, transactionCallBack)
                            }
                            RequestType.REQUEST_TYPE_SALE -> {
                                val amount = "10000"
                                val reserveNumber = "0123456879"//شناسه پرداخت
                                sdkManager.doSaleTransaction(this@MainServicesActivity, amount, reserveNumber, false, transactionCallBack)
                            }
                            RequestType.REQUEST_TYPE_BILL -> {
                                sdkManager.doServiceTransaction(this@MainServicesActivity, RequestType.REQUEST_TYPE_BILL, false, transactionCallBack)
                            }
                            RequestType.REQUEST_TYPE_CHARGE_PIN -> {
                                sdkManager.doServiceTransaction(this@MainServicesActivity, RequestType.REQUEST_TYPE_CHARGE, false, transactionCallBack)
                            }
                            RequestType.REQUEST_TYPE_INQUIRY_TRANSACTION -> {

                                //شناسه برای استعلام تراکنش
                                val trace = "69"
                                val rrn = "320138312569"
                                val reserveNumber = "123465798"

                                // TxnInquiryType به صورت enum تعریف شده است، که براساس نیاز میتوانید مقدار آنرا تغییر دهید
                                val inquiryType = TxnInquiryType.REQUEST_TYPE_INQUIRY_BY_RRN

                                sdkManager.inquiryTransactionData(this@MainServicesActivity, inquiryType, rrn, true, transactionCallBack)
                            }

                            RequestType.REQUEST_TYPE_DO_KEY_CHANGE -> {
                                sdkManager.doConfiguration(this@MainServicesActivity, resultCallBack)
                            }

                            RequestType.REQUEST_TYPE_INQUIRY_POS_DATA -> {
                                sdkManager.inquiryPosData(this@MainServicesActivity, posDataCallBack)
                            }

                            RequestType.REQUEST_TYPE_PRINT_BITMAP -> {
                                // Attention: width of bitmap must be 384 px
                                val options = BitmapFactory.Options()
                                options.inScaled = false
                                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_384, options)
                                sdkManager.printBitmap(this@MainServicesActivity, bitmap, resultCallBack)
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }
}