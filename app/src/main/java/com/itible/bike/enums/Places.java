package com.itible.bike.enums;

public enum Places {
    SELECT_ONE("", "", ""),
    KLOKOCOVA("https://www.google.com/maps/@48.5783671,19.9484727,3a,75y,36.22h,80.59t/data=!3m6!1e1!3m4!1ssvsr7iL5KjqlaWALFXgYZw!2e0!7i13312!8i6656", "48.5783671", "19.9484727"),
    HNUSTA_URAD("https://www.google.com/maps/@48.5796763,19.9534587,3a,75y,85.49h,81.92t/data=!3m6!1e1!3m4!1sOQJgezWw_iPT7oDyhhr0BA!2e0!7i13312!8i6656", "48.5796763", "19.9534587"),
    HNUSTA_SMETISKO("https://www.google.com/maps/@48.5942389,19.9525846,3a,75y,349.96h,91.41t/data=!3m6!1e1!3m4!1syeW8czad23BtHPEXSW3fgA!2e0!7i13312!8i6656", "48.5942389", "19.9525846"),
    TISOVEC_BISTRO("https://www.google.com/maps/@48.6807098,19.943032,3a,75y,338.8h,84.52t/data=!3m6!1e1!3m4!1suSSOf4rN9Y8UykoppoMpaA!2e0!7i13312!8i6656", "48.6807098", "19.943032"),
    HNUSTA_BENZINKA("https://www.google.com/maps/@48.5584358,19.9585675,3a,75y,182.08h,74.62t/data=!3m6!1e1!3m4!1sobuyClSUb-ddYRJCMwOqJw!2e0!7i13312!8i6656", "48.5584358", "19.9585675"),
    POLHORA_JURASKOVCI("https://www.google.com/maps/@49.5118109,19.4564676,3a,75y,147.71h,86.24t/data=!3m6!1e1!3m4!1spU4Qw1VTPQwCEWw3S7iN4Q!2e0!7i13312!8i6656", "49.5118109", "19.4564676"),
    POLHORA_HASICI("https://www.google.com/maps/@49.5092491,19.4592615,3a,75y,142.99h,69.95t/data=!3m6!1e1!3m4!1sc190Y7I_xWTVxonEIksHWw!2e0!7i13312!8i6656", "49.5092491", "19.4592615"),
    POLHORA_SLANA_VODA("https://www.google.com/maps/@49.5261741,19.4729888,3a,75y,24.16h,69.08t/data=!3m6!1e1!3m4!1sLqTBRfMIOvv_c2Kyg51f3w!2e0!7i13312!8i6656", "49.5261741", "19.4729888"),
    POLHORA_BIELA_FARMA("https://www.google.com/maps/@49.5366658,19.398071,3a,75y,200.56h,83.33t/data=!3m6!1e1!3m4!1svWzJxN-VntufYNdLxMiveA!2e0!7i13312!8i6656", "49.5366658", "19.398071");

    private final String url;
    private final String latitude;
    private final String longitude;

    Places(String url, String latitude, String longitude) {
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUrl() {
        return url;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
