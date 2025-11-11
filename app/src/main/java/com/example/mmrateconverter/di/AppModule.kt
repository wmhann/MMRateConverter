import com.google.firebase.firestore.FirebaseFirestore
import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.local.LocalDataSourceImpl
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.data.remote.FirebaseFirestoreDataSource
import com.example.mmrateconverter.data.local.database.AppDatabase // Room Database ကိုပါ ထည့်သွင်း

object AppModule {
    // Room Database ကို ဒီနေရာမှာ Build လုပ်ပါ။
    // val database by lazy {
    //     Room.databaseBuilder(context, AppDatabase::class.java, "rate_db").build()
    // }

    // Remote
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val remoteDataSource: RemoteDataSource by lazy { FirebaseFirestoreDataSource(firestoreInstance) }

    // Local
    // val localDataSource: LocalDataSource by lazy { LocalDataSourceImpl(database.rateDao()) }

    // Repository
    // (နောက်တစ်ဆင့်မှာ ရေးပါမည်)
}