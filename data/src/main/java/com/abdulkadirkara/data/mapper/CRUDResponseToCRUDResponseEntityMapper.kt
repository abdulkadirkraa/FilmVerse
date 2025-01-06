package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import javax.inject.Inject

class CRUDResponseToCRUDResponseEntityMapper @Inject constructor() :
    Mapper<CRUDResponse, CRUDResponseEntity>  {
    override fun map(input: CRUDResponse): CRUDResponseEntity {
        return CRUDResponseEntity(
            success = input.success,
            message = input.message
        )
    }
}