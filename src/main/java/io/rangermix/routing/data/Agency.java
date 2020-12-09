package io.rangermix.routing.data;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.TimeZone;

@Data
public class Agency {
    String id;
    @NotNull String name;
    @NotNull String url;
    @NotNull TimeZone timeZone;
    Locale lang;
    String phone;
    String fareUrl;
    String email;

//    public Agency(@NotNull String id, @NotNull String name, @NotNull String url, @NotNull TimeZone timeZone) {
//        this.id = id;
//        this.name = name;
//        this.url = url;
//        this.timeZone = timeZone;
//    }
//
//    public Agency(@NotNull String id, @NotNull String name, @NotNull String url, @NotNull TimeZone timeZone, Locale lang, String phone, String fareUrl, String email) {
//        this(id, name, url, timeZone);
//        this.lang = lang;
//        this.phone = phone;
//        this.fareUrl = fareUrl;
//        this.email = email;
//    }
}
