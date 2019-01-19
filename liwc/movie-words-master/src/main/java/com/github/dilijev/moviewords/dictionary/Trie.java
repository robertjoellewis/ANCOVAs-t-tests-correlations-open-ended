/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Trie<T> {
	private NodeType type;
	private char c;
	private T payload;

	private HashMap<Character, Trie<T>> children;

	public Trie() {
		type = NodeType.ROOT;
		c = 0; // null character
		payload = null; // the root node doesn't have a payload

		children = new HashMap<>();
	}

	public Trie(NodeType type) {
		this();
		this.type = type;
	}

	private Trie<T> getOrCreateNode(char c) {
		if (children.containsKey(c)) {
			return children.get(c);
		} else {
			Trie<T> node = new Trie<T>(NodeType.NODE);
			node.c = c;

			children.put(c, node);
			return node;
		}
	}

	public void insert(String word, boolean isPrefix, T payload) {
		insertHelper(word.toLowerCase(), isPrefix, payload);
	}

	private void insertHelper(String word, boolean isPrefix, T payload) {
		if (word.length() == 0) {
			if (isPrefix) {
				type = NodeType.PREFIX;
			} else {
				type = NodeType.EXACT;
			}

			this.payload = payload;
		} else {
			Trie<T> node = getOrCreateNode(word.charAt(0));

			word = word.substring(1);
			node.insert(word, isPrefix, payload);
		}
	}

	public T get(String key) {
		boolean endMatch = key.length() == 0 && (this.type == NodeType.EXACT || this.type == NodeType.PREFIX);
		boolean prefixMatch = key.length() != 0 && this.type == NodeType.PREFIX;
		boolean endNoMatch = key.length() == 0 && (this.type == NodeType.NODE || this.type == NodeType.ROOT);

		if (endMatch || prefixMatch) {
			return payload;
		} else if (endNoMatch) {
			return null;
		}

		char c = key.charAt(0);
		Trie<T> child = children.get(c);
		
		if (child == null) {
			return null;
		}

		return child.get(key.substring(1));
	}

	private void getAllStrings(Collection<String> list, String prefix) {
		if (type != NodeType.ROOT) {
			prefix = prefix + c;
		}

		if (type == NodeType.PREFIX) {
			list.add(prefix + "*");
		} else if (type == NodeType.EXACT) {
			list.add(prefix);
		}

		Set<Character> keySet = children.keySet();
		for (Character c : keySet) {
			children.get(c).getAllStrings(list, prefix);
		}
	}

	private Collection<String> getAllStrings() {
		ArrayList<String> list = new ArrayList<>();

		getAllStrings(list, "");

		return list;
	}

	public String toString() {
		return getAllStrings().toString();
	}

	private enum NodeType {
		ROOT, NODE, PREFIX, EXACT
	}
}
