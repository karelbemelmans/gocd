/*
 * Copyright 2016 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.config;

import com.thoughtworks.go.config.builder.ConfigurationPropertyBuilder;
import com.thoughtworks.go.config.validation.NameTypeValidator;
import com.thoughtworks.go.domain.ConfigErrors;
import com.thoughtworks.go.domain.config.Configuration;
import com.thoughtworks.go.domain.config.ConfigurationProperty;

import java.util.Collection;
import java.util.List;

@ConfigTag("pluginRole")
@ConfigCollection(value = ConfigurationProperty.class)
public class PluginRoleConfig extends Configuration implements Role {

    private final ConfigErrors configErrors = new ConfigErrors();

    @ConfigAttribute(value = "name", optional = false)
    protected CaseInsensitiveString name;

    @ConfigAttribute(value = "authConfigId", optional = false)
    private String authConfigId;

    private Users users = new Users();

    public PluginRoleConfig() {
    }

    public PluginRoleConfig(String name, String authConfigId, ConfigurationProperty... properties) {
        super(properties);
        this.name = new CaseInsensitiveString(name);
        this.authConfigId = authConfigId;
    }

    public String getAuthConfigId() {
        return authConfigId;
    }

    public void setAuthConfigId(String authConfigId) {
        this.authConfigId = authConfigId;
    }

    @Override
    public void validate(ValidationContext validationContext) {
        Role.super.validate(validationContext);
        if (!new NameTypeValidator().isNameValid(authConfigId)) {
            configErrors.add("authConfigId", NameTypeValidator.errorMessage("plugin role authConfigId", authConfigId));
        }
    }

    @Override
    public Collection<RoleUser> doGetUsers() {
        return users;
    }

    @Override
    public void doSetUsers(Collection<RoleUser> users) {
        this.users = Users.users(users);
    }

    public ConfigErrors errors() {
        return configErrors;
    }

    public void addError(String fieldName, String message) {
        configErrors.add(fieldName, message);
    }

    @Override
    public CaseInsensitiveString getName() {
        return this.name;
    }

    @Override
    public void setName(CaseInsensitiveString name) {
        this.name = name;
    }

    public void addConfigurations(List<ConfigurationProperty> configurations) {
        ConfigurationPropertyBuilder builder = new ConfigurationPropertyBuilder();
        for (ConfigurationProperty property : configurations) {
            add(builder.create(property.getConfigKeyName(),
                    property.getConfigValue(),
                    null,
                    false));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginRoleConfig)) return false;
        if (!super.equals(o)) return false;

        PluginRoleConfig that = (PluginRoleConfig) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return authConfigId != null ? authConfigId.equals(that.authConfigId) : that.authConfigId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (authConfigId != null ? authConfigId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PluginRoleConfig{" +
                "name=" + name +
                ", authConfigId='" + authConfigId + '\'' +
                '}';
    }
}
