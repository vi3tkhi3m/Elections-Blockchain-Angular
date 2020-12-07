pragma solidity ^0.4.2;

contract Owned {
    address owner;

    constructor() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender==owner);
        _;
    }
}
