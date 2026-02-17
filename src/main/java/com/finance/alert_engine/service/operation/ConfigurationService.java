package com.finance.alert_engine.service.operation;

import com.finance.alert_engine.custom.exception.model.DatabaseException;
import com.finance.alert_engine.dto.request.ConfigurationRequest;
import com.finance.alert_engine.model.Configuration;
import com.finance.alert_engine.repository.ConfigRepository;
import com.finance.alert_engine.util.validate.RequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ConfigurationService {

    private final ConfigRepository configRepository;

    public Configuration createConfig(ConfigurationRequest configReq) {
        RequestValidator.validate(configReq, List.of("name", "value"));

        if(configRepository.findByName(configReq.getName()).isPresent()) {
            throw new DatabaseException("DB-03122", configReq.getName() + " Already exists");
        }

        Configuration configuration = new Configuration();
        configuration.setStatus(true);
        configuration.setCreatedAt(new Date());
        configuration.setName(configReq.getName());
        configuration.setValue(configReq.getValue());
        configRepository.save(configuration);

        return configuration;
    }

}
