package net.idik.lib.cipher.so.generater

import net.idik.lib.cipher.so.extension.KeyExt
import net.idik.lib.cipher.so.utils.AESEncryptor
import net.idik.lib.cipher.so.utils.StringUtils

class CipherSoHeaderBuilder {
    private List<KeyExt> keys
    private String signature = ""
    private String fileName
    private String secretKey = ""

    CipherSoHeaderBuilder(String fileName, List<KeyExt> keys) {
        this.fileName = fileName
        this.keys = keys
    }

    CipherSoHeaderBuilder setSignature(String signature) {
        this.signature = signature
        this
    }

    CipherSoHeaderBuilder setSecretKey(String secretKey) {
        this.secretKey = secretKey
        this
    }

    List<String> build() {
        List<String> lines = new ArrayList<>()
        def headerName = fileName.replaceAll("\\.", "_").replaceAll("-", "_").toUpperCase(Locale.US)
        lines.add("// Auto-Generated By Cipher.so - 林帅斌(me@idik.net)\n\n")
        lines.add("#ifndef $headerName\n")
        lines.add("#define $headerName\n\n")

        lines.add("#define SIGNATURE \"${signature}\"\n")

        lines.add("#define SECRET_KEY \"$secretKey\" \n\n")

        lines.add("#define LOAD_MAP(_map) \\\n")
        keys.each {
//            lines.add("    _map[\"${StringUtils.md5(it.name)}\"] = \"${AESEncryptor.encrypt(secretKey, new String(Base64.encoder.encode(it.value.bytes), "UTF-8"))}\"; \\\n")
            lines.add("    _map[\"${StringUtils.md5(it.name)}\"] = \"${AESEncryptor.encrypt(secretKey, it.value)}\"; \\\n")
//            lines.add("    _map[\"${StringUtils.md5(it.name)}\"] = \"${new String(Base64.encoder.encode(it.value.bytes))}\"; \\\n")
        }
        lines.add("\n")
        lines.add("#endif //$headerName\n\n")
        lines
    }


}