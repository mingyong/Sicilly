package xyz.shaohui.sicilly.data.network.auth;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by kpt on 16/2/23.
 */
public class OAuthToken implements Serializable {

    private static final long serialVersionUID = 3891133932519746686L;

    public static OAuthToken from(final String response) throws IOException {
        return OAuthToken.parse(response);
    }

    public static OAuthToken parse(final String response) {
        OAuthToken token = null;
        try {
            final String[] strs = response.split("&");
            token = new OAuthToken();
            for (final String str : strs) {
                if (str.startsWith("oauth_token=")) {
                    token.setToken(str.split("=")[1].trim());
                } else if (str.startsWith("oauth_token_secret=")) {
                    token.setTokenSecret(str.split("=")[1].trim());
                }
            }
        } catch (final Exception e) {
        }
        return token;
    }

    private String token;

    private String tokenSecret;

    public OAuthToken() {
    }

    public OAuthToken(final String token, final String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthToken)) {
            return false;
        }

        final OAuthToken that = (OAuthToken) o;

        if (!this.token.equals(that.token)) {
            return false;
        }
        if (!this.tokenSecret.equals(that.tokenSecret)) {
            return false;
        }

        return true;
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    @Override
    public int hashCode() {
        int result = this.token.hashCode();
        result = (31 * result) + this.tokenSecret.hashCode();
        return result;
    }

    public boolean isNull() {
        return (this.token == null) || (this.tokenSecret == null);
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setTokenSecret(final String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public String toString() {
        return "OAuthToken{" + "token='" + this.token + '\''
                + ", tokenSecret='" + this.tokenSecret + '}';
    }
}
