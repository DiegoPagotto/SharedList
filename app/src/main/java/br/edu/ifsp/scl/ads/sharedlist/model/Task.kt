package br.edu.ifsp.scl.ads.sharedlist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Task (
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    var title: String = "",
    var description: String = "",
    var creationDate: String = "",
    var expectedFinishDate: String = "",
    var createdBy: String = "",
    var isFinished: Boolean = false,
    var finishedBy: String = "",
): Parcelable