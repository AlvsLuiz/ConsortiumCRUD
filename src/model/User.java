package model;

import java.util.List;

public class User {
	private int id;
	private String name;
	private int age;
	private String email;
	private Consortium consortium;
	private List<Post> post;
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Consortium getConsortium() {
		return consortium;
	}

	public void setConsortium(Consortium consortium) {
		this.consortium = consortium;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}
	
	public List<Post> getPosts() {
		return post;
	}

	public void setPosts(List<Post> post) {
		this.post = post;
	}
	
	public void validate() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome do usuário não pode ser vazio.");
		}
		
		if (consortium == null) {
			throw new IllegalArgumentException("O consórcio está inválido");
		}
		
		if (email == null || email.isBlank() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			throw new IllegalArgumentException("O email do usuário é inválido.");
		}	
	}

	@Override
	public String toString() {
		return name;
	}
}
