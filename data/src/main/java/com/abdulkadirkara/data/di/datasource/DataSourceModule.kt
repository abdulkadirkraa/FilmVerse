package com.abdulkadirkara.data.di.datasource

import com.abdulkadirkara.data.datasource.localdatasource.LocalDataSource
import com.abdulkadirkara.data.datasource.localdatasource.LocalDataSourceImpl
import com.abdulkadirkara.data.datasource.remotedatasource.RemoteDataSource
import com.abdulkadirkara.data.datasource.remotedatasource.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}