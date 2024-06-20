# EMVCo QR Code Decoder and Encoder
It is a simple library to decode and encode [EMVCo QR Code](https://www.emvco.com/emv-technologies/qr-codes/) using Kotlin.

It supports both Consumer-presented mode (CPM) and Merchant-presented mode (MPM) QR Code. 
For the specification, please refer to the [EMVCo QR Code Specification](https://www.emvco.com/specifications/?tax[specifications_categories][32][]=81).

# Features
- [x] Decode EMVCo QR Code,including CPM and MPM
- [x] Encode EMVCo QR Code,including CPM and MPM
- [ ] Validate data field defined in the EMVCo QR Code Specification
- [ ] Decode transparent data like 0x63 and 0x64 in CPM 

# Usage

## Maven
```xml
<dependency>
    <groupId>io.github.nikotung</groupId>
    <artifactId>emv-qrcode-kotlin</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Gradle
```groovy
implementation("io.github.nikotung:emv-qrcode-kotlin:1.0.0")
```

## MPM 
### Decode
```kotlin
val source =
    "00020101021229300012D156000000000510A93FO3230Q31280012D15600000001030812345678520441115802CN5914BEST TRANSPORT6007BEIJING64200002ZH0104最佳运输0202北京540523.7253031565502016233030412340603***0708A60086670902ME91320016A0112233449988770708123456786304A13A"
val merchantPresentMode = MerchantPresentModeDecode(source)
val decode = merchantPresentMode.decode()
```

### Encode
```kotlin
val payload = Tag("00", "01")
val poi = Tag("01", "12")
val amount = Tag("54", "100")
val merchantName = Tag("59", "BEST TRANSPORT")
val merchantCity = Tag("60", "BEIJING")
val merchantCategoryCode = Tag("52", "4111")
val countryCode = Tag("58", "CN")
val currency = Tag("53", "156")


val account00 = Tag("00", "D15600000000")
val account05 = Tag("05", "A93FO3230Q")
val merchantAccountInformation =
    Tag("29", account00.toString() + account05.toString(), subTags = mutableListOf(account00, account05))


val qrcode = MerchantPresentMode(
    mutableMapOf(
        "00" to payload,
        "01" to poi,
        "54" to amount,
        "59" to merchantName,
        "60" to merchantCity,
        "52" to merchantCategoryCode,
        "58" to countryCode,
        "53" to currency,
        "29" to merchantAccountInformation
    )
).toString()
```

## CPM
### Decode
```kotlin
val code = "hQVDUFYwMWEaTwegAAAAVVVVVw8SNFZ4kBI0WNGRIgESNF8="
val modeDecode = ConsumerPresentModeDecode(code)        
val decode = modeDecode.decode()
```

### Encode
```kotlin
val modeEncode = ConsumerPresentMode()

modeEncode.addPayloadFormatIndicator("CPV01")

var byteArray = Format.BINARY.hexByteArray("A0000000555555")

val applicationDefinitionFileName = ConsumerPresentModeCode.APPLICATION_DEFINITION_FILE_NAME
val adf = Binary(applicationDefinitionFileName, byteArray, tag = applicationDefinitionFileName.tag)

val trackData = ConsumerPresentModeCode.TRACK_2_EQUIVALENT_DATA
byteArray = Format.BINARY.hexByteArray("1234567890123458D191220112345F")

val track = Binary(trackData, byteArray, tag = trackData.tag)
val applicationTemplateCode = ConsumerPresentModeCode.APPLICATION_TEMPLATE

val template = Binary(
    applicationTemplateCode,
    adf.getBytes() + track.getBytes(),
    tag = applicationTemplateCode.tag,
    subTags = mutableListOf(adf, track)
)
modeEncode.addTag(template)
val qrcode = modeEncode.toString()
```