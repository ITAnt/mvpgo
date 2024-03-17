//package com.miekir.mt.ui.simple
//
//import com.miekir.mt.databinding.ActivityMainBinding
//import com.miekir.mvp.common.extension.lazy
//import com.miekir.mvp.common.log.L
//import com.miekir.mvp.common.tools.ToastTools
//import com.miekir.mvp.view.binding.adapt.BindingActivity
//
//class SimpleActivity : BindingActivity<ActivityMainBinding>(), ILazyView {
//
//    private val simplePresenter by lazy<SimpleActivity, SimplePresenter>()
//    private val lazyPresenter by lazy<ILazyView, LazyPresenter>()
//
//    override fun onBindingInflate() = ActivityMainBinding.inflate(layoutInflater)
//
//    override fun onInit() {
//        binding.btnTest.setOnClickListener {
////            val testIntent = Intent(4this, MainActivity::class.java)
////            testIntent.putExtra("goKey", "go data")
////            openActivityForResult(testIntent, object: OnResultListener(){
////                override fun onResultOK(backIntent: Intent?) {
////                    ToastUtils.showShort(backIntent?.getStringExtra("backKey"))
////                }
////            })
//
////            simplePresenter.testLazyPresenter()
////            lazyPresenter.testLazyPresenter()
//
////              simplePresenter.testInterface()
//
////            simplePresenter.testCancel()
////            simplePresenter.testOnComplete()
//        }
//    }
//
//    override fun getSizeInDp(): Float {
//        return 375.0f
//    }
//
//
//    fun onLoginResult(success: Boolean, result: String?, errorMessage: String?) {
//        if (!success) {
//            ToastTools.showShort(errorMessage)
//            return
//        }
//        L.d("result:$result")
//        ToastTools.showShort(result)
//    }
//
//    override fun onLazySuccess(result: String) {
//        L.d("result:$result")
//        ToastTools.showShort(result)
//    }
//
////    private fun initClick() {
////        // 开始注册
////        btn_next.setSingleClick {
////            val email = et_email.ifEmpty("邮箱不能为空") ?: return@setSingleClick
////            //registerPresenter.submitRegister(email, code, password, this@RegisterActivity::registerResult)
////            simplePresenter.submitRegister(email, code, password) {
////                if (it.success) {
////                    UserManager.get().user = it.obj
////                    ToastUtils.showShort("注册成功")
////                    finish()
////                }
////            }
////        }
////    }
////
////    private fun registerResult(user: TaskResult<User>) {
////
////    }
//}