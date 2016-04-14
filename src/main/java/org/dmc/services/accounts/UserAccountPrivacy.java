package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class UserAccountPrivacy  {
  
  private UserAccountPrivacy _private = null;
  private UserAccountPrivacy _public = null;

  
  /**
   **/
  @JsonProperty("private")
  public UserAccountPrivacy getPrivate() {
    return _private;
  }
  public void setPrivate(UserAccountPrivacy _private) {
    this._private = _private;
  }

  
  /**
   **/
  @JsonProperty("public")
  public UserAccountPrivacy getPublic() {
    return _public;
  }
  public void setPublic(UserAccountPrivacy _public) {
    this._public = _public;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccountPrivacy userAccountPrivacy = (UserAccountPrivacy) o;
    return Objects.equals(_private, userAccountPrivacy._private) &&
        Objects.equals(_public, userAccountPrivacy._public);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_private, _public);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccountPrivacy {\n");
    
    sb.append("  _private: ").append(_private).append("\n");
    sb.append("  _public: ").append(_public).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
