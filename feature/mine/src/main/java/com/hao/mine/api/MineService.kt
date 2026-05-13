package com.hao.mine.api

import com.hao.proto.*
import com.squareup.wire.Message
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by Li 2022/10/25
 */
interface MineService {

    /**
     * 系统配置相关
     */
    @POST("api/v1/system/gifts")
    fun systemGiftListConfig(@Body req: Message<*, *>?): Observable<*>

//    /**
//     * 用户基本信息
//     */
//    @POST("api/v1/user/mypage")
//    fun getUserMyPage(@Body req: ReqGetMyPage): Observable<ResGetMyPage>
//
    /**
     * 用户主页
     */
    @POST("/club/UserCenterCmd/GET_USER_PAGE")
    fun getUserPage(@Body req: UserIdQuery): Observable<UserPage>

    @POST("/club/LogicCmd/GET_USER_WALL_GIFTS")
    fun getGiftList(@Body req: UserIdQuery): Observable<GiftRecordS>

    @POST("/club/SysCmd/GETCFG_PAY_GOODS")
    fun getGoodList(@Body req: UserIdQuery): Observable<GetcfgPayGoodsRsp>

    @POST("/club/LogicCmd/GET_USER_COIN_DTLS")
    fun getDiamondDetails(@Body req: UserIdQuery): Observable<CoinRecordS>

    @POST("/club/LogicCmd/GET_USER_GIFT_DTLS")
    fun getGiftDetails(@Body req: GetGiftRecordReq): Observable<GiftRecordS>

    @POST("/club/SysCmd/GETCFG_COUNTRY_LIST")
    fun getRegionList(@Body req: IdQuery): Observable<CountryS>
}


