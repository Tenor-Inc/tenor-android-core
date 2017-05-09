package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.CLASS)
@StringDef({SupportMessengers.NONE,
        SupportMessengers.FB_MESSENGER,
        SupportMessengers.WHATSAPP,
        SupportMessengers.MESSAGES,
        SupportMessengers.HANGOUTS,
        SupportMessengers.CHOMP,
        SupportMessengers.SKYPE,
        SupportMessengers.EIGHT_SMS,
        SupportMessengers.TWITTER,
        SupportMessengers.GO_SMS,
        SupportMessengers.TANGO,
        SupportMessengers.KIK,
        SupportMessengers.WE_CHAT,
        SupportMessengers.GOOGLE_MESSENGER,
        SupportMessengers.HIKE,
        SupportMessengers.HTC_MESSAGES,
        SupportMessengers.TELEGRAM,
        SupportMessengers.HOVERCHAT,
        SupportMessengers.VIBER,
        SupportMessengers.KAKAO_TALK,
        SupportMessengers.SLACK,
        SupportMessengers.VODAFONE,
        SupportMessengers.FACEBOOK,
        SupportMessengers.LINE,
        SupportMessengers.REDDIT,
        SupportMessengers.GMAIL,
        SupportMessengers.AOSP_MESSAGES})
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface SupportMessenger {
}
