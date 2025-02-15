package com.uor.eng.cryptx.payload.other;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssociatePhotosRequest {
  private List<String> s3Keys;

}
