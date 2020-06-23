package br.com.cafebinario.teseu.api;

public enum ExecutionStatus {

	Success(1), Error(2);

	private int id;

	private ExecutionStatus(int id) {
		this.id = id;
	}

	public static ExecutionStatus getType(Integer id) {

		if (id == null) {
			return null;
		}

		for (ExecutionStatus position : ExecutionStatus.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id " + id);
	}

	public int getId() {
		return id;
	}

}
