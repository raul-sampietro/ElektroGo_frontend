package elektrogo.front.model
import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var sender : String?,
    var receiver: String?,
    var message : String?,
    var sentAt: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(sender)
        p0?.writeString(receiver)
        p0?.writeString(message)
        p0?.writeString(sentAt)
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

}
