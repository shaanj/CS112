package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node poly3 = null;
		Node curr1 = poly1;
		Node curr2 = poly2;
		Node curr3 = poly3;
		
		while(curr1 != null && curr2 != null) {
			Term t1 = curr1.term;
			Term t2 = curr2.term;
			if (t1.degree == t2.degree) {
				if (poly3 == null) {
					curr3 = new Node(t1.coeff + t2.coeff, t1.degree, null);
					poly3 = curr3;
				} else {
					curr3.next = new Node(t1.coeff + t2.coeff, t1.degree, null);
					curr3 = curr3.next;
				}
				curr1 = curr1.next;
				curr2 = curr2.next;
			} else if (t1.degree > t2.degree) {
				if (poly3 == null) {
					curr3 = new Node(t2.coeff, t2.degree, null);
					poly3 = curr3;
				} else {
					curr3.next = new Node(t2.coeff, t2.degree, null);
					curr3 = curr3.next;
				}
				curr2 = curr2.next;
			} else {
				if (poly3 == null) {
					curr3 = new Node(t1.coeff, t1.degree, null);
					poly3 = curr3;
				} else {
					curr3.next = new Node(t1.coeff, t1.degree, null);
					curr3 = curr3.next;
				}
				curr1 = curr1.next;
			}
		}
		// x can either become curr1 or curr2, if either one becomes null,
		// x is the pointer to the opposite polynomial.
		Node x = null;
		if (curr1 == null)
			x = curr2;
		else if(curr2 == null)
			x = curr1;
		
		while (x != null) {
			if (poly3 == null) { 
				curr3 = new Node(x.term.coeff, x.term.degree, null);
				poly3 = curr3;		
			} else { 
				curr3.next = new Node(x.term.coeff, x.term.degree, null);
				curr3 = curr3.next;
			}
			x = x.next;
		}
		
		return poly3;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node poly3 = null;
		Node curr3 = null;
		Node head3 = null;
		Term t1 = null;
		Term t2 = null;
		for (Node curr1 = poly1; curr1 != null; curr1 = curr1.next) {
			t1 = curr1.term;
			head3 = null;	//head of LL
			curr3 = null;	//tail of LL
			for (Node curr2 = poly2; curr2 != null; curr2 = curr2.next) {
				t2 = curr2.term;
				if (curr3 == null) {
					head3 = new Node(t1.coeff * t2.coeff, t1.degree + t2.degree, null);
					curr3 = head3;
				} else {
					curr3.next = new Node(t1.coeff * t2.coeff, t1.degree + t2.degree, null);
					curr3 = curr3.next;
				}
			}
			poly3 = Polynomial.add(head3, poly3);
		}
		return poly3;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		double sum = 0.0;
		for (Node curr = poly; curr != null; curr = curr.next)
			sum += (Math.pow(x, curr.term.degree) * curr.term.coeff);
		return (float) sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
