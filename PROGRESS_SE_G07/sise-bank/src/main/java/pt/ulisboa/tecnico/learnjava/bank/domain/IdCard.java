package pt.ulisboa.tecnico.learnjava.bank.domain;

public class IdCard {
	private final String firstName;
	private final String lastName;
	private final String nif;
	private final String phoneNumber;
	private final String address;
	private int age;

	public IdCard(String firstName, String lastName, String nif, String phoneNumber, String address, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nif = nif;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNif() {
		return nif;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getAge() {
		return age;
	}

}
