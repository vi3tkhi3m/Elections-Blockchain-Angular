import { Election } from "../domains/votepage/Election";
import { Token } from "../domains/authenticate/token";

export class AuthResponse {
    election: Election;
    token: Token;
}
