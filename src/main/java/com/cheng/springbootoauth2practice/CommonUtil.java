package com.cheng.springbootoauth2practice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * the description of the class TODO
 *
 * @author CTPlayer
 **/
public class CommonUtil {
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAldNP8jjl56Aew7yKyrtk/fB8+Z75S6Ni" +
            "/wP0XWtwzGd3vmvGdZ5vYgqxlu5CSqUQ2OW+dBacIfl3mK1oM5N8kD2UrFy8LnutJoC4t+hxyFDrUfBVfhrNjX8AN6d4yXc9W4uDvP+AW3ZzQlUCzybfCQqxi+V0xUDbpsb5SMmV+R0PVINYsz/BjVIn43ij09C73M+ZpOPCETGDdOXhsZ4OXNDEJKI9QZgk/g6i+hOpByBkBUZHa0TckqwpNS8aD6bFid8pSzjvxD+uS0EB7uYnlSSfzPZqJaU6QVl2RoONEoJCooqO2l+toMWCG44XVwAAjfyb9qiVjR2rrhP/fr7LSQIDAQAB";
    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCV00" +
            "/yOOXnoB7DvIrKu2T98Hz5nvlLo2L/A/Rda3DMZ3e+a8Z1nm9iCrGW7kJKpRDY5b50Fpwh+XeYrWgzk3yQPZSsXLwue60mgLi36HHIUOtR8FV+Gs2NfwA3p3jJdz1bi4O8/4BbdnNCVQLPJt8JCrGL5XTFQNumxvlIyZX5HQ9Ug1izP8GNUifjeKPT0Lvcz5mk48IRMYN05eGxng5c0MQkoj1BmCT+DqL6E6kHIGQFRkdrRNySrCk1LxoPpsWJ3ylLOO/EP65LQQHu5ieVJJ/M9molpTpBWXZGg40SgkKiio7aX62gxYIbjhdXAACN/Jv2qJWNHauuE/9+vstJAgMBAAECggEAfCJPW7ZOrcFZm/0r1Vq8CzXkpgFUNZoSnI2POpOZKpITgT7UeIEjcczAZPfVDvmCYuK/EHnnNLL3pxUNf6YUD4ImG0TeWXj49iyyXxWEqF57CjGg89GLO0/dQz9LcUiNGrsWFFJw69bRGkVLBY0EP/6PKgSb4g6hrqWuxuLWkkV/Ae7+Qyze7VjPFVWxRCMJxvS4mMLknbJXd9SpRndNLUFBSukdWvDfBYkAsytwIVQcTAMOjewrsXl3cKX9yTrxf38QgNx/X1tJLNwqKPv05TBTi9kjhEi7mMjpiHA+YiKJxc9AR5BcVPpmn8xUNOGcw7VbYqWcrr5/ty4O1gqvAQKBgQDTbPK8FrsXJR0aAHbtg8VASG/xknpmZd7izc/UwSWNdzKleHi3T9/9g89kS6iFeJSCWb8vsx8YYpM8bIJ2W4GHguIHAs9kA7HHwN6F8iA79FThveP4UJ6LB81NHEAFfY6pZaWwjUEXssAOaxV0VvhDv5aelY+KNIL8fNH10s0nKQKBgQC1aa8iJd+wzcVrSHKl/wIdeu5e45ozLo8/X/JGf+39O8CD5Ru9t9T9NXqwyfEgnuZ9OWFHUrajtd6jZ53p4nyl8MVScqtohAK0APJexSUD45V2GnDF+07KaWJvTZY+eF8uqqaPL5HguwdwSzq4KcKkOP2zmsp0Wn+oa0NgbNmnIQKBgBm30v8WNMPn+9ZB4DWcJc5gAV39V25FTguAxZs3s3211i9SrDyANtr7waqNmX5C4K3KhPskas/ojch32pbTIrp9LUl1Cg08dc+6olbg0RL58alUE/sMs3xuAocyvIbucwAgITuMszJSyBH47K8uNRzonUXFI6TIJnbYKCIi9lmhAoGBAK8XKo4lyckmQTVzfhwSjOMRJAH3YHuno2BCZV5/2J4BzqQ+O1EXnX0p4ZnBvnWCh+kyuV4SSe6l+RTzS+lRzIaIZJpXzHIaf1VmWIb5delT/Yw4psGe9QPNRNEyLCFtIkizMgCepAgn210ZbroSrCa+TrbLXj2AnxdZN3VYeZWhAoGAZxYaCh6HBXFZfSvcaVEs/N6QuzAdYLUSnDgZu4XVVM3NiU8ic6iWCIl2Z25A1SghYUdGrNt7y2RbPLnQhTbpKNmpOnFgCDCQSYXRkZnQbvnl84MNyYzn8u16DSYEZg4ehepqM8LDakX0svmMS59WPcOygEjLkUxvJgSYDZBtCJ8=";


    public static PublicKey generatePublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey generatePrivateKey(String privateKeyStr) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateKeyPair() {
        try {
            return new KeyPair(generatePublicKey(PUBLIC_KEY), generatePrivateKey(PRIVATE_KEY));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
