package dependencies.dto;

public class InvitationAcceptanceDTO {
    private String status;

    public InvitationAcceptanceDTO() {}

    public InvitationAcceptanceDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
