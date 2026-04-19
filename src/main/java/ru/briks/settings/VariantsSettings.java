package ru.briks.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.briks.dto.VariantDto;

import java.util.List;
import java.util.Map;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 20:33
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "download")
public class VariantsSettings {
    private Map<String, List<VariantDto>> variants;
}
