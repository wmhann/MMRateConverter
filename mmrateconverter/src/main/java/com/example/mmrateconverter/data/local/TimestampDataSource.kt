package com.example.mmrateconverter.data.local


    interface TimestampDataSource {
        // နောက်ဆုံး Rates (သို့မဟုတ်) Gold Prices ကို Fetch လုပ်ခဲ့သည့် အချိန်ကို သိမ်းဆည်းခြင်း
        suspend fun saveLastFetchTime(key: String, time: Long)

        // နောက်ဆုံး Fetch အချိန်ကို ရယူခြင်း (မရှိရင် 0L ပြန်ပေးမည်)
        suspend fun getLastFetchTime(key: String): Long
    }
