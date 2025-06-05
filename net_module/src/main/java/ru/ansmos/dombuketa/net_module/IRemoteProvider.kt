package ru.ansmos.dombuketa.net_module

import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2

interface IRemoteProvider {
    fun provideTags() : IDomBuketaApi2
}