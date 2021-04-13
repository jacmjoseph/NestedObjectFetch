package com.kpmg.project3.NestedObjectFetch;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NestedObjectFetchApplicationTests {

	private NestedObjectFetchApplication app;

	@Test
	public void correctStringAndKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", "x/y/z");
		Assertions.assertThat("a".equals(app.getKeyValue()));
	}

	@Test
	public void invalidNestedStringAndCorrectKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a", "x/y/z");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Deserialization failed");
	}

	@Test
	public void validNestedStringAndLongKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", "x/y/z/u");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}
	
	@Test
	public void validNestedStringAndInvalidTokenizer() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", "x:y:z");
		Assertions.assertThat(app.getKeyValue()==null);
	}	

	@Test
	public void validNestedStringAnd2LevelShortKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", "x/y");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void nullNestedStringAndValidKey() {
		app = new NestedObjectFetchApplication(null, "x/y/z");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void validNestedStringAndNullKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", null);
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void emptyNestedStringAndValidKey() {
		app = new NestedObjectFetchApplication("", "x/y/z");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void validNestedStringAndEmptyKey() {
		app = new NestedObjectFetchApplication("{'x':{'y':{'z':'a'}}}", "");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void emptyNestedStringAndEmptyKey() {
		app = new NestedObjectFetchApplication("", "");
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}

	@Test
	public void nullNestedStringAndnullKey() {
		app = new NestedObjectFetchApplication(null, null);
		Assertions.assertThatThrownBy(() -> {
			app.getKeyValue();
		}).isInstanceOf(CustomException.class).hasMessageContaining("Object fetch failed");
	}
}
