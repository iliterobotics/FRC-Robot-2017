package robotcode
import spock.lang.*



// Hit 'Run Script' below
class MyFirstSpec extends Specification {
  def "let's try this!"() {
    expect:
    Math.max(1, 2) == 2
  }
  
  def "THis will break. "() { 
	  
	  expect:
	  Math.max(1,2) ==2
	 
  }
}