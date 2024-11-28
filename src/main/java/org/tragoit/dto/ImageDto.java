package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;
import org.tragoit.model.Trip;

@Getter
@Setter
public class ImageDto {
    private Long id;
    private String url;
    private Trip trip;
}
