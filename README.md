# 💰MMRateConverter - မြန်မာငွေဈေးနှင့် ရွှေဈေး App

MMRateConverter သည် မြန်မာကျပ်ငွေ (MMK) နှင့် အခြားနိုင်ငံသုံးငွေကြေးများ၏ နောက်ဆုံးရ ငွေလဲနှုန်းများကို ကြည့်ရှုနိုင်ပြီး၊ တွက်ချက်မှုများ ပြုလုပ်နိုင်ရန်အတွက် တည်ဆောက်ထားသော Android Application တစ်ခုဖြစ်ပါသည်။
ဤ Project သည် ခေတ်မီ Android Development နည်းပညာများဖြစ်သော Jetpack Compose, Dagger Hilt, Coroutines, Flow နှင့် Clean Architecture တို့ကို အသုံးပြု၍ ရေးသားထားပါသည်။
## ✨အဓိက Features များ
* နောက်ဆုံးရ ငွေလဲနှုန်းများ - နောက်ဆုံးရရှိနိုင်သော ငွေလဲနှုန်းများကို ကြည့်ရှုနိုင်သည်။
* Favorite ပြုလုပ်ခြင်း - မိမိစိတ်ဝင်စားသော ငွေကြေးများကို Favorite အဖြစ် သတ်မှတ်၍ အလွယ်တကူ ပြန်ကြည့်နိုင်သည်။
* ငွေလဲနှုန်း တွက်ချက်ခြင်း - ရွေးချယ်ထားသော ငွေကြေးနှင့် မြန်မာကျပ်ငွေအကြား လွယ်ကူစွာ တွက်ချက်နိုင်သည်။
* Offline Support - Favorite လုပ်ထားသော ငွေကြေးများကို Offline အခြေအနေတွင် ကြည့်ရှုနိုင်သည်။

## Screenshots
| Rates Screen                     | Calculator Screen                             | 
<img src="link/to/your/screenshot1.png" width="250"> | <img src="link/to/your/screenshot2.png" width="250"> |

## Architecture

ဤ Project ကို Clean Architecture ပုံစံဖြင့် တည်ဆောက်ထားပြီး တာဝန်ယူမှုများကို Layer အလိုက် ခွဲခြားထားသည့် Multi-module ချဉ်းကပ်မှုကို အသုံးပြုထားပါသည်။
Java
+--------------------------------------------------------------------------+
|                                   :app                                   |
|--------------------------------------------------------------------------|
|   Presentation Layer (Jetpack Compose, ViewModel, Compose Navigation)    |
|                                    +                                     |
|                      Dependency Injection (Hilt)                         |
+--------------------------------------------------------------------------+
|
| Depends on
v
+--------------------------------------------------------------------------+
|                             :mmrateconverter                             |
|--------------------------------------------------------------------------|
|     Domain Layer (UseCases, Entities) - Core Business Logic (Pure Kotlin) |
|--------------------------------------------------------------------------|
|       Data Layer (Repository, Remote/Local DataSources, DTOs)            |
+--------------------------------------------------------------------------+

### Modules
* :app - Application ၏ အဓိက Module ဖြစ်ပြီး UI Layer (presentation) နှင့် Dependency Injection (di) တို့ကို တာဝန်ယူသည်။ Android Framework နှင့် သက်ဆိုင်သော အစိတ်အပိုင်းများ ပါဝင်သည်။
* :mmrateconverter - Android Framework နှင့် မသက်ဆိုင်သော Core Logic ကို ထိန်းသိမ်းထားသည့် Kotlin Library Module ဖြစ်သည်။ domain နှင့် data layer များ ပါဝင်ပြီး ပြန်လည်အသုံးပြုနိုင်ရန် (reusable) နှင့် သီးခြားစမ်းသပ်နိုင်ရန် (testable) တည်ဆောက်ထားသည်။
### Dependency Rule
Dependency Flow သည် Presentation -> Domain <- Data ဟူသော လမ်းကြောင်းအတိုင်း စီးဆင်းသည်။ :app module က :mmrateconverter module ကို မှီခိုပြီး၊ :mmrateconverter module အတွင်းရှိ domain layer သည် မည်သည့် layer ကိုမှ မှီခိုခြင်းမရှိပါ။

## အသုံးပြုထားသော နည်းပညာနှင့် Library များ
* Language: Kotlin
* UI: Jetpack Compose - Android အတွက် ခေတ်မီ UI toolkit
* Architecture:
* Clean Architecture
* MVVM (Model-View-ViewModel)
* Multi-module
* Asynchronous: Kotlin Coroutines & Flow
* Dependency Injection: Hilt
* Navigation: Compose Navigation
* Networking: Retrofit 2 & OkHttp 3
* Local Storage: Room - SQLite object mapping library
* JSON Parsing: Gson
* ViewModel: Jetpack ViewModel
* State Management: StateFlow

## 💡Project ကို Build ပြုလုပ်ခြင်း

ဤ Project ကို သင့်စက်ထဲတွင် Clone လုပ်ပြီး Build ပြုလုပ်ရန် အောက်ပါအဆင့်များကို လိုက်နာပါ။
1. Repository ကို Clone လုပ်ပါ:
```bash
git clone [https://github.com/wmhann/mmrateconverter.git](https://github.com/wmhann/MMRateConverter.git)
```
2. Android Studio တွင် ဖွင့်ပါ: Android Studio (နောက်ဆုံးထွက်ဗားရှင်း) ကိုဖွင့်ပြီး mmrateconverter project folder ကို ရွေးချယ်၍ ဖွင့်ပါ။
3. Build လုပ်ပါ: Gradle sync ပြီးဆုံးအောင်စောင့်ပြီးနောက် "Build" -> "Make Project" ကိုနှိပ်ပါ သို့မဟုတ် toolbar မှ Run 'app' နှိပ်၍ build လုပ်ပြီး run ပါ။
   ဤ project သည် သင်ယူလေ့လာရန် ရည်ရွယ်ပြီး စတင်ဖန်တီးထားခြင်း ဖြစ်ပါသည်။

