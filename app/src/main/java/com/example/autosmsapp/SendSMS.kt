package com.example.autosmsapp

import android.content.Context
import android.os.Build
import android.telephony.SmsManager



class SendSMS {
      private fun sendText(context:Context, phoneNumber: String) {

          //for different android versions
          val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= 23) {
             context.getSystemService(SmsManager::class.java)
          } else {
             SmsManager.getDefault()
          }

          val message = "Testing "

         smsManager.sendTextMessage(phoneNumber, null, message, null, null)
     }
}