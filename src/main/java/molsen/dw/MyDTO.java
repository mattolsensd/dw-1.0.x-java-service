package molsen.dw;

public class MyDTO {

    private final Long id;
    private final String name;

    public MyDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyDTO)) return false;

        MyDTO myDTO = (MyDTO) o;

        if (id != null ? !id.equals(myDTO.id) : myDTO.id != null) return false;
        return name != null ? name.equals(myDTO.name) : myDTO.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MyDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
