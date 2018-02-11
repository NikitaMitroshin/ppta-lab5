package model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StackParams {

	private final int limit;
	private final int index;

}
