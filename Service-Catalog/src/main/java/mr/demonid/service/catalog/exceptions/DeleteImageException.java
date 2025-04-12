package mr.demonid.service.catalog.exceptions;

public class DeleteImageException extends CatalogException {

    private String message;

    public DeleteImageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка удаления файла: " + message;
    }
}
