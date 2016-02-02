package org.dmc.services.services.specifications;

public class Trial {

	private final String logTag = Trial.class.getName();
    private final int successes;
    private final int fails;
    
    public Trial(int successes, int fails) {
        this.successes = successes;
        this.fails = fails;
    }
    
    public int getSuccesses() {
        return successes;
    }
    
    public int getFails() {
    	return fails;
    }
    
    @Override
    public String toString(){
    	
    	return "Successes: " + successes + "\nFails: " + fails;
    }
}