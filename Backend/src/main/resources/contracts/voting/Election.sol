pragma solidity ^0.4.21;

import "./DateTime.sol";
import "./Owned.sol";


contract Election is Owned{

  DateTime dateTime = new DateTime();

  // Election variables
  uint id;
  string public name;
  uint public startDate;
  uint public endDate;

  // Party struct
  struct Party {
    uint16 id;
    string name;
    uint voteCount;
  }

  // Candicate struct
  struct Member {
    uint id;
    uint position;
    string lastname;
    string initials;
    string firstname;
    string gender;
    string location;
    uint partyID;
    uint voteCount;
  }

  constructor() public {

  }


  // Store voters tokens
  mapping(string => bool) private voters;
  // Keeps count of voters
  uint public votersCount;

  // This is where the parties are stored
  mapping(uint => Party) private parties;

  // Keeps count of parties
  uint16 public partiesCount = 0;

  // This is where the members are stored
  mapping(uint => Member) private members;
  // Keeps count of members
  uint public membersCount = 0;

  // Only the owner can set the election name
  function setElectionName(string _name) onlyOwner public {
      name = _name;
  }

  // Only the owner can set the startDate
  function setStartDate(uint16 _year, uint8 _month, uint8 _day, uint8 _hour) onlyOwner public {
      startDate = dateTime.toTimestamp(_year,_month,_day,_hour);
  }

  // Only the owner can set the endDate
  function setEndDate(uint16 _year, uint8 _month, uint8 _day, uint8 _hour) onlyOwner public {
      endDate = dateTime.toTimestamp(_year,_month,_day,_hour);
  }

  // Only the owner can add parties to the election
  function addParty(string _name) onlyOwner public {
    parties[partiesCount] = Party(partiesCount, _name, 0);
    partiesCount++;
  }

  // Only the owner can add members to the parties
  function addMember(uint _position, string _lastname, string _initials, string _firstname, string _gender, string _location, uint _partyID) onlyOwner public {
    members[membersCount] = Member(membersCount, _position, _lastname, _initials, _firstname, _gender, _location, _partyID, 0);
    membersCount++;
  }

  function getMemberInfo(uint _memberID) view public returns (uint, string, string, string, string, string, uint){
    return (members[_memberID].position, members[_memberID].lastname, members[_memberID].initials, members[_memberID].firstname, members[_memberID].gender, members[_memberID].location, members[_memberID].partyID);
  }


  function getPartyInfo(uint _partyID) view public returns(uint, string){
    return (parties[_partyID].id, parties[_partyID].name);
  }

  function vote(uint member_id, string _token) onlyOwner public {
    // Require that they haven't voted before
    require(!voters[_token]);
    // Require that the vote is before endTime
    require(now >= startDate && now < endDate);
    // Require valid members
    require(member_id >= 0 && member_id < membersCount);

    // Record that voter has voted;
    voters[_token] = true;

    // Record vote that voter submitted;
    parties[getPartyId(member_id)].voteCount++;
    members[member_id].voteCount++;
  }

  function getPartyId(uint _memberID) view private returns (uint) {
    return members[_memberID].partyID;
  }

  function getPartyResults(uint _partyID) view public returns (string, uint) {
    // Only show results when election is ended.
    require(now >= endDate);

    return (parties[_partyID].name, parties[_partyID].voteCount);
  }

  function getMemberResults(uint _memberID) view public returns (string, string, string, uint, uint) {
    // Only show results when election is ended.
    require(now >= endDate);

    return (members[_memberID].firstname, members[_memberID].initials, members[_memberID].lastname,  members[_memberID].partyID, members[_memberID].voteCount);
  }

  //Functions written by Marijn.
  function getNumberOfParties() view public returns (uint16){
    return partiesCount;
  }

  function getPartyNameByPartyID(uint _partyID) view public returns (string) {
    return parties[_partyID].name;
  }

}