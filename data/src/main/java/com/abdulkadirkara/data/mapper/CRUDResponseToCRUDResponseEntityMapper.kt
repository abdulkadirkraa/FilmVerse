package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import javax.inject.Inject

/**
 * A mapper class for converting a `CRUDResponse` object to a `CRUDResponseEntity` object.
 *
 * This class is used to transform data received from the remote data source (DTO)
 * into a domain model, ensuring the data is ready for use within the application's domain layer.
 *
 * @constructor Injected using Hilt to simplify dependency management.
 */
class CRUDResponseToCRUDResponseEntityMapper @Inject constructor() :
    Mapper<CRUDResponse, CRUDResponseEntity> {

    /**
     * Maps a `CRUDResponse` object to a `CRUDResponseEntity` object.
     *
     * @param input The `CRUDResponse` object received from the remote data source.
     * @return A `CRUDResponseEntity` object containing the mapped data.
     */
    override fun map(input: CRUDResponse): CRUDResponseEntity {
        return CRUDResponseEntity(
            success = input.success,
            message = input.message
        )
    }
}
