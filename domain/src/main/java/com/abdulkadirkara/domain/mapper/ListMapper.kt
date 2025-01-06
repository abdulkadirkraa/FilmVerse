package com.abdulkadirkara.domain.mapper

interface ListMapper<I, O> {
    fun map(input: List<I>): List<O>
}