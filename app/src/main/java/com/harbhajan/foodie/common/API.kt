package com.harbhajan.foodie.common

object API {

    /*
    * phone=
    * password=
    * */

    private const val BASE_URL = "https://admin.manadelivery.in/admin/"

    fun getLoginUrl(userId: String, userPassword: String, fcm: String?): String =
        "${BASE_URL}restaurant_login/?userid=${userId}&password=${userPassword}&fcm=${fcm}"

    fun getUpdateUrl(userId: String, userPhone: String, userPassword: String): String =
        "${BASE_URL}restaurant_updatePass/?userid=${userId}&phone=${userPhone}&password=${userPassword}"

    const val IMAGE_URL = "${BASE_URL}img/"

    fun getOrders(id: String) = "${BASE_URL}orders_restaurant/?rid=${id}"

    fun getStatus(id: String, status: String): String =
        "${BASE_URL}restaurant_status/?rid=$id&status=$status"

}