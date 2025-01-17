package com.abdulkadirkara.domain.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}