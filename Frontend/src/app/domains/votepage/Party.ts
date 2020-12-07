import {Candidate} from './Candidate';
import { DataService } from '../../services/data.service';

export interface Party {
  candidates: Candidate[];
  name: string;
  id: number;
}
