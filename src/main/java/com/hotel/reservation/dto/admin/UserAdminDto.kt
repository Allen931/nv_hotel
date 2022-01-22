package com.hotel.reservation.dto.admin

import com.hotel.reservation.type.UserLoyaltyType
import com.hotel.reservation.type.UserPermissionType
import javax.validation.constraints.NotBlank

class UserAdminDto(
    val name: String? = null,

    val permission: UserPermissionType? = null,

    val loyalty: UserLoyaltyType? = null
) {}