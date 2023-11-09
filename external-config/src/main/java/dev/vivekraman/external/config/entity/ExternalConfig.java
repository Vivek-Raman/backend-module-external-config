package dev.vivekraman.external.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Table(name = "external_config")
public class ExternalConfig implements Serializable {
  @Serial
  private static final long serialVersionUID = -2929520433624352081L;

  @Id
  private String key;
  private String value;
  private String notes;
}
