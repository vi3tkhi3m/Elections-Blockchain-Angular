import { Party } from "../domains/election-result/Party";
import { Member } from "../domains/election-result/Member";

export class ElectionResultResponse {
    name: string;
    parties: Array<Party>;
    members: Array<Member>;
}