package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.domain.Bug
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

interface RoomRepository {
    fun getAllBugsFromDbByDate(date: String): Flow<List<Bug>>

    fun getAllBugsFromDbByOsAndDate(opSys:String, date: String): Flow<List<Bug>>

    fun saveBugsToDb(bugs: ArrayList<Bug>)
}