package com.hao.login.api

import com.hao.proto.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Created by wanghao 2022/7/27
 */

interface LoginService {
    @POST()
    fun login(@Body req: ThirdPlatLoginReq): Observable<ThirdPlatLoginRsp>

    @POST("/club/UserCenterCmd/THIRD_PLAT_LOGIN")
    fun thirdLogin(@Body req: ThirdPlatLoginReq): Observable<ThirdPlatLoginRsp>

    @POST("/club/UserCenterCmd/SENDME_SIGN_CODE")
    fun sendSignCode(@Body req: SendMeSignCodeReq): Observable<SendMeSignCodeRsp>

    @POST("/club/UserCenterCmd/CHECK_SIGN_CODE")
    fun checkSignCode(@Body req: CheckSignCodeReq): Observable<CheckSignCodeRsp>

    @POST("/club/LogicCmd/SENDME_CODE_BY")
    fun sendCode(@Body req: CheckVerifyCode): Observable<UtilRet>

    @POST("/club/LogicCmd/CHECKDO_VERIFY_CODE")
    fun checkCode(@Body req: CheckVerifyCode): Observable<UtilRet>

    @POST("/club/UserCenterCmd/GET_USER_INFO")
    fun getUserInfo(@Body req: UserIdQuery): Observable<UserInfo>

    @POST("/club/UserCenterCmd/WEB_TOKEN_LOGIN")
    fun tokenLogin(@Body req: WebTokenLoginReq): Observable<WebTokenLoginRsp>

}