package Try_it.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ResDTO<T> {

    private Integer statusCode;
    private String message;
    private T data;

    public ResDTO(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Builder
    public ResDTO(Integer statusCode, String message, T data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
